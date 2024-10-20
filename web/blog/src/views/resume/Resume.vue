<template>
  <div>
    <!-- banner -->
    <div class="banner" :style="cover">
      <h1 class="banner-title">个人简历</h1>
    </div>
    <!-- 链接列表 -->
    <v-card class="blog-container">
      <iframe width="100%" height="700" src="https://laoyujianli.com/i_share/murphy"  border="0" frameborder="no" framespacing="0" allowfullscreen="true"></iframe>
    </v-card>
  </div>
</template>

<script>
export default {
  components: {
  },
  created() {
    this.listFriendLink();
    this.listComments();
  },
  data: function() {
    return {
      friendLinkList: [],
      commentList: [],
      count: 0
    };
  },
  methods: {
    listFriendLink() {
      this.axios.get("/api/links").then(({ data }) => {
        this.friendLinkList = data.data;
      });
    },
    listComments() {
      this.axios
        .get("/api/comments", {
          params: { current: 1 }
        })
        .then(({ data }) => {
          this.commentList = data.data.recordList;
          this.count = data.data.count;
        });
    }
  },
  computed: {
    blogInfo() {
      return this.$store.state.blogInfo;
    },
    cover() {
      var cover = "";
      this.$store.state.blogInfo.pageList.forEach(item => {
        if (item.pageLabel == "link") {
          cover = item.pageCover;
        }
      });
      return "background: url(" + cover + ") center center / cover no-repeat";
    }
  }
};
</script>

<style scoped>
blockquote {
  line-height: 2;
  margin: 0;
  font-size: 15px;
  border-left: 0.2rem solid #49b1f5;
  padding: 10px 1rem !important;
  background-color: #ecf7fe;
  border-radius: 4px;
}
.link-title {
  color: #344c67;
  font-size: 21px;
  font-weight: bold;
  line-height: 2;
}
.link-container {
  margin: 10px 10px 0;
}
.link-wrapper {
  position: relative;
  transition: all 0.3s;
  border-radius: 8px;
}
.link-avatar {
  margin-top: 5px;
  margin-left: 10px;
  transition: all 0.5s;
}
@media (max-width: 759px) {
  .link-avatar {
    margin-left: 30px;
  }
}
.link-name {
  text-align: center;
  font-size: 1.25rem;
  font-weight: bold;
  z-index: 1000;
}
.link-intro {
  text-align: center;
  padding: 16px 10px;
  height: 50px;
  font-size: 13px;
  color: #1f2d3d;
  width: 100%;
}
.link-wrapper:hover a {
  color: #fff;
}
.link-wrapper:hover .link-intro {
  color: #fff;
}
.link-wrapper:hover .link-avatar {
  transform: rotate(360deg);
}
.link-wrapper a {
  color: #333;
  text-decoration: none;
  display: flex;
  height: 100%;
  width: 100%;
}
.link-wrapper:hover {
  box-shadow: 0 2px 20px #49b1f5;
}
.link-wrapper:hover:before {
  transform: scale(1);
}
.link-wrapper:before {
  position: absolute;
  border-radius: 8px;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background: #49b1f5 !important;
  content: "";
  transition-timing-function: ease-out;
  transition-duration: 0.3s;
  transition-property: transform;
  transform: scale(0);
}
</style>
